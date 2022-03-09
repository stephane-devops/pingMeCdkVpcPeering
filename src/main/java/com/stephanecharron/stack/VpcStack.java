package com.stephanecharron.stack;

import com.stephanecharron.stackProps.VpcStackProps;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;

import java.util.ArrayList;
import java.util.List;

public class VpcStack extends Stack {

    public VpcStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    private final List<Vpc> vpcList= new ArrayList<>();

    @lombok.Builder
    public VpcStack(final Construct scope, final String id, final VpcStackProps props) {
        super(scope, id, props);

        int index = 0;
        for (String cidrs: props.getVpcSetup().getCidrs()){

            SubnetConfiguration subnetConfigurationPrivate = SubnetConfiguration.builder()
                    .name("private-sub-vpc-"+index)
                    .subnetType(SubnetType.PRIVATE_WITH_NAT)
                    .cidrMask(27)
                    .build();
            SubnetConfiguration subnetConfigurationPublic = SubnetConfiguration.builder()
                    .name("public-sub-vpc-"+index)
                    .subnetType(SubnetType.PUBLIC)
                    .cidrMask(27)
                    .build();

            Vpc vpc = Vpc.Builder.create(this, "vpcVpnPing-"+index)
                    .subnetConfiguration(
                            com.sun.tools.javac.util.List.of(
                                    subnetConfigurationPrivate,subnetConfigurationPublic
                            )
                    )
                    .cidr(cidrs)
                    .maxAzs(props.getVpcSetup().getMaxAzs())
                    .build();
            index++;
            vpcList.add(vpc);
        }

        index = 0;
        for (Vpc vpc: vpcList){
            SecurityGroup.fromSecurityGroupId(this, "DefaultSecurityGroup-"+index, vpc.getVpcDefaultSecurityGroup())
                    .addIngressRule(Peer.anyIpv4(), Port.icmpPing(),"ping rule");
            index++;
        }
    }
    public List<Vpc> getVpcList() {
        return vpcList;
    }
}
