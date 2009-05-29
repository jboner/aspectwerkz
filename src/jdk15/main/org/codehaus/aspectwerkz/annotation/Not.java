package org.codehaus.aspectwerkz.annotation;

import java.lang.annotation.Annotation;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public @interface Not {
    Class<? extends Annotation> value(); // DOES NOT WORK!
}
//@interface And {
//    String value();
//}
//
//
////@Not(
//// @And(
////    "Deprecated.class")
//// )
////class Foo {}
//
////@interface Gen{}
//
//@interface Ping {
//    Pong value();
//}
//@interface Pong {//extends Gen{
//    String value();
//}
//
//@Ping(
//        @Pong("dd")
//        )
//class Foo{}
