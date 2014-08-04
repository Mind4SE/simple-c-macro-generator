/**
 * Copyright (C) 2014 Schneider Electric
 *
 * This file is part of "Mind Compiler" is free software: you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: mind@ow2.org
 *
 * Authors: Julien TOUS
 * Contributors: Stephane SEYVOZ
 */

package org.ow2.mind.adl;

import org.ow2.mind.adl.implementation.BasicImplementationLocator;
import org.ow2.mind.adl.implementation.ImplementationLocator;
import org.ow2.mind.adl.simple.SimpleADLDefinitionSourceGenerator;
import org.ow2.mind.adl.simple.SimpleAnnotation;
import org.ow2.mind.adl.simple.SimpleDefinitionCompiler;
import org.ow2.mind.adl.simple.SimpleDefinitionSourceGeneratorDispatcher;
import org.ow2.mind.adl.simple.SimpleGraphCompiler;
import org.ow2.mind.idl.simple.SimpleIDLDefinitionSourceGenerator;
import org.ow2.mind.inject.AbstractMindModule;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

public class SimpleADLBackendModule extends AbstractMindModule {

	protected void configureImplementationLocator() {
		bind(ImplementationLocator.class).annotatedWith(Names.named(SimpleADLDefinitionSourceGenerator.SIMPLE_HEADER)).to(BasicImplementationLocator.class);
	}

	//-- Component Definition Generators

	/**
	 * Returns the {@link Multibinder} that can be used to add
	 * {@link DefinitionSourceGenerator}.
	 * 
	 * @param binder the local binder.
	 * @return the {@link Multibinder} that can be used to add
	 *         {@link DefinitionSourceGenerator}.
	 */
	public static Multibinder<DefinitionSourceGenerator> getDefinitionSourceGeneratorMultiBinder(
			final Binder binder) {
		
		/*
	     * We use a SimpleAnnotation to differentiate THIS MultiBinder from the
	     * standard MultiBinder. Otherwise, the Google Guice MultiBinder API (at
	     * least in version 2.0) gives you the DefinitionSourceGenerator that the
	     * MultiBinder already populated in the Standard backend Module. This means
	     * the "addBinding" calls results were concatenated, leading to queue the
	     * 'Simple' backend source generation tasks in the delegation chain, meaning our
	     * code generation would be called first, then OVERWRITTEN by the standard
	     * backend source generators afterwards (first in the delegation chain =
	     * last called, like a stack). We discriminate the code generators injection
	     * in the SimpleDefinitionSourceGeneratorDispatcher by using our custom
	     * @SimpleAnnotation on the injection field, recognized by Google Guice as it
	     * implements @BindingAnnotation. For more information, see
	     * "public static Multibinder<T> newSetBinder (Binder binder, Class<T> type, Annotation annotation)"
	     * at:
	     * http://google-guice.googlecode.com/svn/trunk/latest-javadoc/com/google
	     * /inject/multibindings/Multibinder.html and
	     * http://code.google.com/p/google-guice/wiki/BindingAnnotations The same
	     * way was used for instance code generation.
	     */
	    final Multibinder<DefinitionSourceGenerator> setBinder = Multibinder
	        .newSetBinder(binder, DefinitionSourceGenerator.class,
	            SimpleAnnotation.class);
		
		return setBinder;
	}

	protected void configureDefinitionSourceGenerator() {
		bind(DefinitionSourceGenerator.class).to(SimpleDefinitionSourceGeneratorDispatcher.class);
		
		final Multibinder<DefinitionSourceGenerator> setBinder = getDefinitionSourceGeneratorMultiBinder(binder());
		setBinder.addBinding().to(SimpleADLDefinitionSourceGenerator.class);
		setBinder.addBinding().to(SimpleIDLDefinitionSourceGenerator.class);
	}

	//-- Mock backend with our tasks (do not use standard compilation, link etc)

	protected void configureGraphCompiler() {
		bind(GraphCompiler.class).to(SimpleGraphCompiler.class);
	}

	protected void configureDefinitionCompiler() {
		bind(DefinitionCompiler.class).to(SimpleDefinitionCompiler.class);
	}
}
