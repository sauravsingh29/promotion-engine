package org.saurav.processor;

import lombok.NonNull;
import org.saurav.constant.PromoType;
import org.saurav.dto.Promotion;

import java.util.List;

/**
 * @author Saurav Singh
 **/
public interface PromoOfferProcessor<I, O> {

    O process(final @NonNull I input, final @NonNull List<Promotion> promotions);

    PromoType type();
}
