/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.RowEntity;

/**
 * @author guochen
 */
public class RowEntityComparator<T extends RowEntity>
{
	public static class CompareListPair<T extends RowEntity>
	{
		private List<T> list1;
		private List<T> list2;

		/**
		 * @param list1
		 * @param list2
		 */
		public CompareListPair(List<T> list1, List<T> list2)
		{
			super();
			this.list1 = list1;
			this.list2 = list2;
		}

		/**
		 * @return the list1
		 */
		public List<T> getList1()
		{
			return list1;
		}

		/**
		 * @return the list2
		 */
		public List<T> getList2()
		{
			return list2;
		}

	}

	/**
	 * Compare the 2 list of elements, and return a pair of lists, that contains elements inside one (original) list but not
	 * inside another list
	 *
	 * @param list1
	 * @param list2
	 * @return
	 */
	public CompareListPair<T> compare(List<T> list1, List<T> list2)
	{
		// these are the target list that contains the compared result: difference between 2 lists
		List<T> tList1 = null, tList2 = null;

		Set<T> set1 = new HashSet<T>();
		if (list1 != null) {
			set1.addAll(list1);
		}
		if (list2 != null) {
			for (Iterator<T> itr2 = list2.iterator(); itr2.hasNext();) {
				T item = itr2.next();
				if (set1.contains(item)) {
					set1.remove(item);
					itr2.remove();
				}
			}
			if (list2.isEmpty()) {
				list2 = null;
			}
		}
		tList2 = list2;
		if (!set1.isEmpty()) {
			tList1 = new ArrayList<T>();
			tList1.addAll(set1);
		}
		return new CompareListPair<T>(tList1, tList2);
	}
}
