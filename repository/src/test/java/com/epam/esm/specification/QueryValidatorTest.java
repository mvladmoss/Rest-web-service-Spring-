package com.epam.esm.specification;

import com.epam.esm.exception.IncorrectSqlQueryParametersException;
import org.junit.Test;

public class QueryValidatorTest {

    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String SORT_BY_NAME = "name";
    private static final String SORT_BY_ID = "id";
    private static final String CREATE_DATE = "date_of_creation";
    private static final String MODIFICATION_DATE = "date_of_modification";

    @Test
    public void checkSortCorrectSortByName() {
        QueryValidator.checkSort(SORT_BY_NAME, ASC);
    }

    @Test(expected = IncorrectSqlQueryParametersException.class)
    public void checkSortInCorrectSortOrder() {
        QueryValidator.checkSort(SORT_BY_NAME, "dsa");
    }

    @Test
    public void checkSortCorrectSortByID() {
        QueryValidator.checkSort(SORT_BY_ID, DESC);
    }

    @Test(expected = IncorrectSqlQueryParametersException.class)
    public void checkSortInCorrectSortBy() {
        QueryValidator.checkSort("dsa", ASC);
    }

    @Test(expected = IncorrectSqlQueryParametersException.class)
    public void checkSortIncorrectSortOrderWithoutSortBy() {
        QueryValidator.checkSort(null, ASC);
    }

}
