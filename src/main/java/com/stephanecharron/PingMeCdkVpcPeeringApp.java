package com.stephanecharron;

import com.stephanecharron.model.VpcSetup;
import com.stephanecharron.stack.InstanceStack;
import com.stephanecharron.stack.PeeringStack;
import com.stephanecharron.stack.VpcStack;
import com.stephanecharron.stackProps.VpcListStackProps;
import com.stephanecharron.stackProps.VpcStackProps;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;

public class PingMeCdkVpcPeeringApp {
    public static void main(final String[] args) {
        App app = new App();

        Environment environment = Environment.builder()
                .region((String) app.getNode().tryGetContext("region"))
                .account((String) app.getNode().tryGetContext("account"))
                .build();

        String [] cidrs = {"10.0.0.0/24","10.0.1.0/24"};

        VpcStackProps vpcStackProps = VpcStackProps.builder()
                .environment(environment)
                .vpcSetup(VpcSetup.builder()
                        .maxAzs(1)
                        .cidrs(cidrs)
                        .build())
                .build();

        VpcStack vpcStack = VpcStack.builder()
                .scope(app)
                .id("VpcStack")
                .props(vpcStackProps)
                .build();

        VpcListStackProps vpcListStackProps = VpcListStackProps.builder()
                .environment(environment)
                .vpcs(vpcStack.getVpcList())
                .build();

        InstanceStack.builder()
                .scope(app)
                .id("InstancePeersStack")
                .props(vpcListStackProps)
                .build();

        PeeringStack.builder()
                .scope(app)
                .id("PeeringStack")
                .props(vpcListStackProps)
                .build();

        app.synth();
    }
}

