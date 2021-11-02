package ch.m1m.hz.proxy;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;
import io.undertow.util.Headers;

import java.net.URI;
import java.net.URISyntaxException;

public class Proxy {

    public static void main(final String[] args) {

        try {
            // this are 3 embedded dummy servers
            //

            /*
            final Undertow server1 = Undertow.builder()
                    .addHttpListener(8090, "localhost")
                    .setHandler(new HttpHandler() {

                @Override
                public void handleRequest(HttpServerExchange exchange) throws Exception {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Server1");
                }
            }).build();
            server1.start();
            */

            /*
            final Undertow server2 = Undertow.builder()
                    .addHttpListener(8095, "localhost")
                    .setHandler(new HttpHandler() {

                @Override
                public void handleRequest(HttpServerExchange exchange) throws Exception {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Server2");
                }
            }).build();
            server2.start();
            */

            LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient()
                    .addHost(new URI("http://localhost:8090"))
                    .addHost(new URI("http://localhost:8095"))
                    .setConnectionsPerThread(20);

            Undertow reverseProxy = Undertow.builder()
                    .addHttpListener(8080, "localhost")
                    .setIoThreads(4)
                    .setHandler(new ProxyHandler(loadBalancer, 30000, ResponseCodeHandler.HANDLE_404))
                    .build();
            reverseProxy.start();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
