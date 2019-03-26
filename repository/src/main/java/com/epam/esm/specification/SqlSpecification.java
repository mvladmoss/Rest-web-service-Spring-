package com.epam.esm.specification;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public interface SqlSpecification {
    String toSql();

    SqlParameterSource params();
}
