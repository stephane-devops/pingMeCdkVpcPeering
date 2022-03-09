package com.stephanecharron.stackProps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class VpcListStackProps implements StackProps {
    final private List<Vpc> vpcs;
    final private Environment environment;

    @Override
    public @Nullable Environment getEnv() {
        return this.environment;
    }
}
