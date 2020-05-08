/*
 * Copyright (c) 1999, 2005, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package agape.tools;

/**
 * A generic class for pairs.
 *
 * <p>
 * <b>This is NOT part of any supported API. If you write code that depends on
 * this, you do so at your own risk. This code and its internal interfaces are
 * subject to change or deletion without notice.</b>
 */
public class Pair<A, B> {

	public final A fst;
	public final B snd;

	public Pair(A fst, B snd) {
		this.fst = fst;
		this.snd = snd;
	}

	@Override
	public String toString() {
		return "Pair[" + this.fst + "," + this.snd + "]";
	}

	private static boolean equals(Object x, Object y) {
		return (x == null && y == null) || (x != null && x.equals(y));
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Pair<?, ?> && Pair.equals(this.fst, ((Pair<?, ?>) other).fst)
				&& Pair.equals(this.snd, ((Pair<?, ?>) other).snd);
	}

	@Override
	public int hashCode() {
		if (this.fst == null)
			return (this.snd == null) ? 0 : this.snd.hashCode() + 1;
		else if (this.snd == null)
			return this.fst.hashCode() + 2;
		else
			return this.fst.hashCode() * 17 + this.snd.hashCode();
	}

	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<A, B>(a, b);
	}
}
