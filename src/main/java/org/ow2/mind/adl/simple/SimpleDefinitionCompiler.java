/**
 * Copyright (C) 2014 Schneider-Electric
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
 * Authors: Stephane SEYVOZ
 * Contributors: 
 */

package org.ow2.mind.adl.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Definition;
import org.ow2.mind.adl.DefinitionCompiler;
import org.ow2.mind.adl.DefinitionSourceGenerator;
import org.ow2.mind.adl.ast.ImplementationContainer;
import org.ow2.mind.compilation.CompilationCommand;

import com.google.inject.Inject;

/**
 * A "mock" class to only generate code and never return tasks
 * to be scheduled for execution.
 * Inspired by {@link org.ow2.mind.BasicDefinitionCompiler}.
 *  
 * @author Stephane SEYVOZ
 *
 */
public class SimpleDefinitionCompiler implements DefinitionCompiler {

	@Inject
	protected DefinitionSourceGenerator definitionSourceGeneratorItf;

	// ---------------------------------------------------------------------------
	// Implementation of the Visitor interface
	// ---------------------------------------------------------------------------

	public Collection<CompilationCommand> visit(final Definition definition,
			final Map<Object, Object> context) throws ADLException {

		// Handle only primitives
		if (definition instanceof ImplementationContainer) {
			/*
			 * This will use our {@link org.ow2.mind.adl.simple.SimpleHeaderGenerator}
			 * bound in our {@link org.ow2.mind.adl.SimpleADLBackendModule}
			 */
			definitionSourceGeneratorItf.visit(definition, context);
		}

		final Collection<CompilationCommand> result = new ArrayList<CompilationCommand>();

		// We only generate files, no command to be scheduled and executed

		return result;
	}

}
