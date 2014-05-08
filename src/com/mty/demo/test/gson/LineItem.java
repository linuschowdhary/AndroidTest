/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mty.demo.test.gson;

/**
 * A line item in a cart. This is not a rest resource, just a dependent object
 * 
 * @author Inderjeet Singh
 */
public class LineItem {
	private String name;
	private int quantity;
	private long priceInMicros;
	private String currencyCode;

	public LineItem() {

	}

	public LineItem(String name, int quantity, long priceInMicros,
			String currencyCode) {
		this.name = name;
		this.quantity = quantity;
		this.priceInMicros = priceInMicros;
		this.currencyCode = currencyCode;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public long getPriceInMicros() {
		return priceInMicros;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	@Override
	public String toString() {
		return "LineItem [name=" + name + ", quantity=" + quantity
				+ ", priceInMicros=" + priceInMicros + ", currencyCode="
				+ currencyCode + "]";
	}

	// @Override
	// public String toString() {
	// return String.format("(item: %s, qty: %s, price: %.2f %s)",
	// name, quantity, priceInMicros/(double)1000000, currencyCode);
	// }

}
