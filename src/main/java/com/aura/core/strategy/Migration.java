package com.aura.core.strategy;

import com.aura.extractor.Extractor;
import com.aura.transformer.Transformer;

public interface Migration {
    void execute();

    // This defines the "Abstract Factory" behavior
    Extractor getExtractor();
    Transformer getTransformer();

    // You can add metadata here if needed
    String getMigrationType();
}
