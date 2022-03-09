package com.stephanecharron.stack;

import com.stephanecharron.stackProps.VpcListStackProps;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;

public class InstanceStack extends Stack {
    public InstanceStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    @lombok.Builder
    public InstanceStack(final Construct scope, final String id, final VpcListStackProps props) {
        super(scope, id, props);

        int index =0;
        for (Vpc vpc: props.getVpcs()){
            String instanceName = String.format("Instance-%s", index);
            BastionHostLinux instance = BastionHostLinux.Builder.create(this, instanceName)
                    .instanceName(instanceName)
                    .instanceType(InstanceType.of(InstanceClass.BURSTABLE3,InstanceSize.MICRO))
                    .vpc(vpc)
                    .securityGroup(
                            SecurityGroup.fromSecurityGroupId(this,String.format("%sSecurityGroup",instanceName), vpc.getVpcDefaultSecurityGroup()
                    ))
                    .build();

            CfnOutput.Builder.create(this,String.format("%sPrivateIp",instance))
                    .value(instance.getInstancePrivateIp())
                    .build();
            index++;
        }
    }
}
