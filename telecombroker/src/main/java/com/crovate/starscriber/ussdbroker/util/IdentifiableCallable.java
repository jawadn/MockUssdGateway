/**
 * Custom Callable Interface with Id.
 *
 */
package com.crovate.starscriber.ussdbroker.util;


import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 *  @author umermansoor
 */
public interface IdentifiableCallable<T> extends Callable<T> 
{
    byte getId();
    void cancelTask(); // Method for supporting non-standard cancellation
    RunnableFuture<T> newTask();
}
