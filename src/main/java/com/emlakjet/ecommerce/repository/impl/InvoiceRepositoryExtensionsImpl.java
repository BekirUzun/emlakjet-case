package com.emlakjet.ecommerce.repository.impl;

import com.emlakjet.ecommerce.dto.internal.AmountSumResult;
import com.emlakjet.ecommerce.repository.InvoiceRepositoryExtensions;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class InvoiceRepositoryExtensionsImpl implements InvoiceRepositoryExtensions {

    private final MongoTemplate mongoTemplate;


    /**
     * Calculates the total amount of invoices with optional given parameters.
     *
     * @param userId     the ID of the user whose invoices are to be summed.
     * @param isApproved the approval status filter for the invoices. If null, invoices are not filtered by approval status.
     * @return the total amount of invoices matching the criteria or 0 if no invoices are found.
     */
    @Override
    public Double invoiceAmountTotal(@Nullable String userId, @Nullable Boolean isApproved) {
        var stages = new ArrayList<AggregationOperation>();

        if (isApproved != null) {
            stages.add(Aggregation.match(new Criteria("isApproved").is(isApproved)));
        }
        if (StringUtils.isNotEmpty(userId)) {
            stages.add(Aggregation.match(new Criteria("createdBy").is(userId)));
        }
        stages.add(Aggregation.group("createdBy")
                .sum("amount")
                .as("totalAmount"));

        var aggregation = Aggregation.newAggregation(stages);
        var aggregationResults = mongoTemplate.aggregate(aggregation, "invoice", AmountSumResult.class);
        var result = aggregationResults.getUniqueMappedResult();
        return result != null ? result.getTotalAmount() : 0D;
    }
}
