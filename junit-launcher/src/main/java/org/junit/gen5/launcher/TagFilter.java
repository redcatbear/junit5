/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.gen5.launcher;

import static java.util.Arrays.asList;
import static org.junit.gen5.commons.meta.API.Usage.Experimental;

import java.util.List;
import java.util.stream.Stream;

import org.junit.gen5.commons.meta.API;
import org.junit.gen5.commons.util.Preconditions;
import org.junit.gen5.engine.FilterResult;
import org.junit.gen5.engine.TestDescriptor;
import org.junit.gen5.engine.TestTag;

/**
 * Factory methods for creating {@link PostDiscoveryFilter PostDiscoveryFilters}
 * based on <em>require</em> and <em>exclude</em> tags.
 *
 * @since 5.0
 */
@API(Experimental)
public final class TagFilter {

	private TagFilter() {
		/* no-op */
	}

	/**
	 * Create a <em>require</em> filter based on the supplied {@code tags}.
	 *
	 * <p>Containers and tests will only be executed if they are tagged with
	 * at least one of the supplied <em>require</em> tags.
	 *
	 * @param tags the required tags; never {@code null} or empty
	 */
	public static PostDiscoveryFilter requireTags(String... tags) {
		Preconditions.notNull(tags, "tags must not be null");
		return requireTags(asList(tags));
	}

	/**
	 * Create a <em>require</em> filter based on the supplied {@code tags}.
	 *
	 * <p>Containers and tests will only be executed if they are tagged with
	 * at least one of the supplied <em>require</em> tags.
	 *
	 * @param tags the require tags; never {@code null} or empty
	 */
	public static PostDiscoveryFilter requireTags(List<String> tags) {
		Preconditions.notEmpty(tags, "tags must not be null or empty");
		return descriptor -> FilterResult.includedIf(trimmedTagsOf(descriptor).anyMatch(tags::contains));
	}

	/**
	 * Create an <em>exclude</em> filter based on the supplied {@code tags}.
	 *
	 * <p>Containers and tests will only be executed if they are <em>not</em>
	 * tagged with any of the supplied <em>exclude</em> tags.
	 *
	 * @param tags the exclude tags; never {@code null} or empty
	 */
	public static PostDiscoveryFilter excludeTags(String... tags) {
		Preconditions.notNull(tags, "tags must not be null");
		return excludeTags(asList(tags));
	}

	/**
	 * Create an <em>exclude</em> filter based on the supplied {@code tags}.
	 *
	 * <p>Containers and tests will only be executed if they are <em>not</em>
	 * tagged with any of the supplied <em>exclude</em> tags.
	 *
	 * @param tags the exclude tags; never {@code null} or empty
	 */
	public static PostDiscoveryFilter excludeTags(List<String> tags) {
		Preconditions.notEmpty(tags, "tags must not be null or empty");
		return descriptor -> FilterResult.includedIf(trimmedTagsOf(descriptor).noneMatch(tags::contains));
	}

	private static Stream<String> trimmedTagsOf(TestDescriptor descriptor) {
		// @formatter:off
		return descriptor.getTags().stream()
				.map(TestTag::getName)
				.map(String::trim);
		// @formatter:on
	}

}
