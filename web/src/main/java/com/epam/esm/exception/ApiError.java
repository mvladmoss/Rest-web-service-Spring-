package com.epam.esm.exception;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class ApiError {

    private List<String> messages;

    public ApiError(List errorMessage) {
        this.messages = errorMessage;
    }

    public List<String> getErrorMessage() {
        return messages;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.messages = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ApiError that = (ApiError) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(messages, that.messages)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(messages)
                .toHashCode();
    }
}
