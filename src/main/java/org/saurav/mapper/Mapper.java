package org.saurav.mapper;

import lombok.NonNull;

/**
 * @author Saurav Singh
 **/
public interface Mapper<I, O> {

    @NonNull O map(final @NonNull I i);
}
