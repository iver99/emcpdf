/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.nls;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author reliang
 */
public class ResourceBundleEnumeration implements Enumeration<String> {
    Set<String> set;
    Iterator<String> iterator;
    Enumeration<String> enumeration;

    public ResourceBundleEnumeration(Set<String> set, Enumeration<String> enumeration) {
        this.set = set;
        iterator = set.iterator();
        this.enumeration = enumeration;
    }

    String next = null;

    @Override
    public boolean hasMoreElements() {
        if (next == null) {
            if (iterator.hasNext()) {
                next = ((String) iterator.next());
            } else if (enumeration != null) {
                while ((next == null) && (enumeration.hasMoreElements())) {
                    next = ((String) enumeration.nextElement());
                    if (set.contains(next)) {
                        next = null;
                    }
                }
            }
        }
        return next != null;
    }

    @Override
    public String nextElement() {
        if (hasMoreElements()) {
            String result = next;
            next = null;
            return result;
        }
        throw new NoSuchElementException();
    }
}