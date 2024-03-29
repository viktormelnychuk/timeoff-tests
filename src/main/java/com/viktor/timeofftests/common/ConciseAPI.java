package com.viktor.timeofftests.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class ConciseAPI {
    public abstract WebDriver getDriver();
    public void open(String url){
        getDriver().get(url);
    }
    class ProxiedWebElement implements WebElement {

        public void click() {

        }

        public void submit() {

        }

        public void sendKeys(CharSequence... charSequences) {

        }

        public void clear() {

        }

        public String getTagName() {
            return null;
        }

        public String getAttribute(String s) {
            return null;
        }

        public boolean isSelected() {
            return false;
        }

        public boolean isEnabled() {
            return false;
        }

        public String getText() {
            return null;
        }

        public List<WebElement> findElements(By by) {
            return null;
        }

        public WebElement findElement(By by) {
            return null;
        }

        public boolean isDisplayed() {
            return false;
        }

        public Point getLocation() {
            return null;
        }

        public Dimension getSize() {
            return null;
        }

        public String getCssValue(String s) {
            return null;
        }
    }

    class ElementFinderProxy implements InvocationHandler {
        private By elementLocator;

        private ElementFinderProxy(By elementLocator){
            this.elementLocator = elementLocator;
        }
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try{
                return method.invoke(new WebDriverWait(getDriver(), 2)
                        .until(ExpectedConditions.visibilityOfElementLocated(elementLocator)), args);
            } catch (InvocationTargetException e){
                throw e.getCause();
            }
        }
    }

    private Object newElementFinderProxyInstance (Object obj, By elementLocator){
        return java.lang.reflect.Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new ElementFinderProxy(elementLocator));
    }

    protected WebElement findOne(By locator){
        Logger log = LogManager.getLogger(this.getClass());
        log.debug("Waiting for visibility of element by {}", locator);
        return (WebElement) newElementFinderProxyInstance(new ProxiedWebElement(), locator);
    }

    class ListOfWebElementsBait implements List<WebElement>{

        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean contains(Object o) {
            return false;
        }

        public Iterator<WebElement> iterator() {
            return null;
        }

        public Object[] toArray() {
            return new Object[0];
        }

        public <T> T[] toArray(T[] a) {
            return null;
        }

        public boolean add(WebElement element) {
            return false;
        }

        public boolean remove(Object o) {
            return false;
        }

        public boolean containsAll(Collection<?> c) {
            return false;
        }

        public boolean addAll(Collection<? extends WebElement> c) {
            return false;
        }

        public boolean addAll(int index, Collection<? extends WebElement> c) {
            return false;
        }

        public boolean removeAll(Collection<?> c) {
            return false;
        }

        public boolean retainAll(Collection<?> c) {
            return false;
        }

        public void clear() {

        }

        public WebElement get(int index) {
            return null;
        }

        public WebElement set(int index, WebElement element) {
            return null;
        }

        public void add(int index, WebElement element) {

        }

        public WebElement remove(int index) {
            return null;
        }

        public int indexOf(Object o) {
            return 0;
        }

        public int lastIndexOf(Object o) {
            return 0;
        }

        public ListIterator<WebElement> listIterator() {
            return null;
        }

        public ListIterator<WebElement> listIterator(int index) {
            return null;
        }

        public List<WebElement> subList(int fromIndex, int toIndex) {
            return null;
        }
    }


    class ElementsFinderProxy implements InvocationHandler {

        private By elementsLocator;

        private ElementsFinderProxy (By elementsLocator){
            this.elementsLocator = elementsLocator;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                return method.invoke(new WebDriverWait(getDriver(), 2)
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementsLocator)), args);
            } catch (InvocationTargetException e){
                throw e.getCause();
            }
        }
    }

    private Object newElementsFinderProxyInstance(Object obj, By elementsLocator){
        return java.lang.reflect.Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new ElementsFinderProxy(elementsLocator)
        );
    }

    protected List<WebElement> findAllBy(By locator){
        Logger log = LogManager.getLogger(this.getClass());
        log.debug("Waiting for visibility of elements by {}", locator);
        //noinspection unchecked
        return (List<WebElement>) newElementsFinderProxyInstance(new ListOfWebElementsBait(), locator);
    }
}
