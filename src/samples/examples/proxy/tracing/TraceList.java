package examples.proxy.tracing;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.aspectwerkz.proxy.Proxy;

public class TraceList {

    public static void main(String[] args) {
        List list = (List) Proxy.newInstance(ArrayList.class);
        list.add("test");
        String test = (String) list.get(0);
        list.clear();
    }
}
