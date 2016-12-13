#!/usr/bin/python

from mininet.topo import Topo
from mininet.node import RemoteController
from mininet.net import Mininet
from mininet.log import setLogLevel, info
from mininet.cli import CLI
from mininet.clean import cleanup

import socket

class FloodlightController(RemoteController):
    def __init__(self, name, ip='127.0.0.1', port=6653, **kwargs):
        RemoteController.__init__(self, name, ip, port, **kwargs)

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

def get_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(('10.255.255.255', 0))
        ip = s.getsockname()[0]
    except:
        ip = '127.0.0.1'
    finally:
        s.close()
    return ip

def run():
    ''' Method used to configure network '''
    # Create network
    net = Mininet(topo=SimpleTopology(), controller=FloodlightController(name='floodlight', ip=CONTROLLER_IP), autoSetMacs=True)
    # Add NAT connectivity
    net.addNAT().configDefault()
    # Start Network
    net.start()
    
    # Deploy interactive client
    CLI(net)
    
    # Shutdown network
    net.stop() 
    # Cleanup
    cleanup()

if __name__ == '__main__':
    global CONTROLLER_IP, CONTROLLER_PORT
    CONTROLLER_IP = get_ip()
    CONTROLLER_PORT = '5000'
    
    setLogLevel('info')
    run()
    
