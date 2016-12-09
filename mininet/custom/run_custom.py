#!/usr/bin/python

from mininet.topo import Topo
from mininet.node import RemoteController
from mininet.net import Mininet
from mininet.log import setLogLevel, info
from mininet.cli import CLI

class FloodlightController(RemoteController):
    def __init__(self, name, **kwargs):
        RemoteController.__init__(self, name, ip='127.0.0.1', port=6653, **kwargs)

class SimpleTopology(Topo):
    ''' Single switch connected to n hosts '''
    def __init__(self, n=2, **opts):
        # Initialize topology
        Topo.__init__(self, **opts)
        
        # Add switch
        switch = self.addSwitch('s1')
        
        for h in range(n):
            # Add hosts
            host = self.addHost('h%s' % (h + 1))
            # Add links
            self.addLink(host, switch)

def run():
    ''' Method used to configure network '''
    # Create network
    net = Mininet()

    # Create Topology
    net.buildFromTopo(SimpleTopology())
    # Expect remote controller
    net.addController(FloodlightController('c0')
    # Add NAT connectivity
    net.addNAT().configDefault()

    # Start Network
    net.start()
    
    # Deploy interactive client
    CLI(net)
    
    # Shutdown network
    net.stop() 

if __name__ == '__main__':
    setLogLevel('info')
    run()
    
