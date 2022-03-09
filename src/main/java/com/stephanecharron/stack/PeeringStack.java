package com.stephanecharron.stack;

import com.stephanecharron.stackProps.VpcListStackProps;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.CfnRoute;
import software.amazon.awscdk.services.ec2.CfnVPCPeeringConnection;
import software.amazon.awscdk.services.ec2.ISubnet;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

public class PeeringStack extends Stack {
    public PeeringStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    @lombok.Builder
    public PeeringStack(final Construct scope, final String id, final VpcListStackProps props) {
        super(scope, id, props);

        Vpc vpc0 = props.getVpcs().get(0);
        Vpc vpc1 = props.getVpcs().get(1);

        int index = 0;

        CfnVPCPeeringConnection peer = CfnVPCPeeringConnection.Builder.create(this, "Peer")
                .vpcId(vpc0.getVpcId())
                .peerVpcId(vpc1.getVpcId())
                .build();

        for (ISubnet subnet : vpc0.getPrivateSubnets()) {
            CfnRoute.Builder
                    .create(this, "RouteFromPrivateSubnetOfVpc1ToVpc2" + index)
                    .destinationCidrBlock(vpc1.getVpcCidrBlock())
                    .routeTableId(subnet.getRouteTable().getRouteTableId())
                    .vpcPeeringConnectionId(peer.getRef())
                    .build();
            index++;
        }

        for (ISubnet subnet : vpc1.getPrivateSubnets()) {
            CfnRoute.Builder
                    .create(this, "RouteFromPrivateSubnetOfVpc2ToVpc1" + index)
                    .destinationCidrBlock(vpc0.getVpcCidrBlock())
                    .routeTableId(subnet.getRouteTable().getRouteTableId())
                    .vpcPeeringConnectionId(peer.getRef())
                    .build();
            index++;
        }
    }
}
