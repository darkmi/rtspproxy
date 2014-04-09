RtspProxy
==========

RtspProxy is a proxy server for multimedia streaming services based on the RTSP protocol.

The current version is a complete rewrite from scratch in Java of previous versions based on C++. The goal is to build a robust and scalable system usable in production environment.
The proxy is based on an asynchronous network framework, Apache MINA , which is built on Java NIO. This framework does permit to RtspProxy to handle high loads and concurrent users.

http://rtspproxy.berlios.de/index.html
