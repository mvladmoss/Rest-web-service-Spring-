package com.epam.esm.specification;

import com.epam.esm.exception.IncorrectSqlQueryParametersException;
import org.apache.commons.lang3.StringUtils;

public class QueryValidator {

    private static final String ORDER_WITHOUT_SORT_BY = "sort.order.without.sortBy";
    private static final String INCORRECT_SORT_ORDER = "incorrect.sort.order.parameter";
    private static final String INCORRECT_SORT_BY = "incorrect.sort.parameter";

    public static void checkSort(String sortBy, String sortOrder) {
        if (StringUtils.isEmpty(sortBy) && !StringUtils.isEmpty(sortOrder)) {
            throw new IncorrectSqlQueryParametersException(ORDER_WITHOUT_SORT_BY);
        }
        if (!StringUtils.isEmpty(sortOrder) && !StringUtils.isEmpty(sortBy) && !sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc")) {
            throw new IncorrectSqlQueryParametersException(INCORRECT_SORT_ORDER, sortOrder);
        }

        if (!StringUtils.isEmpty(sortBy) && !sortBy.equalsIgnoreCase("date_of_creation") &&
                !sortBy.equalsIgnoreCase("date_of_modification") && !sortBy.equalsIgnoreCase("name")
                && !sortBy.equalsIgnoreCase("id")) {
            throw new IncorrectSqlQueryParametersException(INCORRECT_SORT_BY, sortBy);
        }
    }
}
