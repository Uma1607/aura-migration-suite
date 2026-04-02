package com.aura.core.strategy;

import com.aura.extractor.Extractor;
import com.aura.extractor.LookerExtractor;
import com.aura.transformer.PowerBiTransformer;
import com.aura.transformer.Transformer;
import org.springframework.stereotype.Component;

@Component("pbi")
public class PowerBIMigrationStrategy implements Migration {
    private final LookerExtractor extractor;
    private final PowerBiTransformer transformer;

    public PowerBIMigrationStrategy(LookerExtractor extractor, PowerBiTransformer transformer) {
        this.extractor = extractor;
        this.transformer = transformer;
    }

    @Override
    public void execute() {

    }

    @Override
    public Extractor getExtractor() {
        return this.extractor;
    }

    @Override
    public Transformer getTransformer() {
        return this.transformer;
    }

    @Override
    public String getMigrationType() {
        return this.toString();
    }
}