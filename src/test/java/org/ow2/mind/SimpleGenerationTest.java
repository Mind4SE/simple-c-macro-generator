/**
 * Copyright (C) 2012 Schneider-Electric
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
 * Contact: mind@ow2.org, sseyvoz@assystem.com
 *
 * Authors: Stéphane Seyvoz, Assystem (for Schneider-Electric)
 * Contributors: 
 */

package org.ow2.mind;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.util.FractalADLLogManager;
import org.ow2.mind.AbstractSimpleGenerationTest;
import org.ow2.mind.adl.annotation.predefined.Run;
import org.ow2.mind.annotation.AnnotationHelper;
import org.testng.annotations.Test;

public class SimpleGenerationTest extends AbstractSimpleGenerationTest {

	private static final String ADL_EXT = ".adl.h";
	private static final String ITF_EXT = ".itf.h";
	private static final String MAKE_EXT = ".make";
	protected static Logger logger = FractalADLLogManager.getLogger("simple-generation-test");

	@Override
	protected void initPath() {
		File header = new File("src/assemble/resources/runtime/");
		initSourcePath(getDepsDir("fractal/api/Component.itf").getAbsolutePath(),header.getAbsolutePath(),"src");
	}

	/**
	 * Server interface with C native type only.
	 */
	@Test(groups = {"simpleGeneration"})
	public void basicTypesServerTest()
			throws Exception {
		String usedItfs[] = {"common.BasicTypes"};
		compileADL("common.BasicTypeServer",usedItfs);
	}

	/**
	 * Client interface with C native type only.
	 */
	@Test(groups = {"simpleGeneration"})
	public void basicTypesClientTest()
			throws Exception {
		String usedItfs[] = {"common.BasicTypes"};
		compileADL("common.BasicTypeClient",usedItfs);
	}

	/**
	 * Server interface with typedef definition.
	 */
	@Test(groups = {"simpleGeneration"})
	public void typedefServerTest()
			throws Exception {
		String usedItfs[] = {"common.Typedef"};
		compileADL("common.TypedefServer",usedItfs);
	}

	/**
	 * Client interface with typedef definition.
	 */
	@Test(groups = {"simpleGeneration"})
	public void typedefClientTest()
			throws Exception {
		String usedItfs[] = {"common.Typedef"};
		compileADL("common.TypedefClient",usedItfs);
	}

	/**
	 * Server interface with struct definition.
	 */
	@Test(groups = {"simpleGeneration"})
	public void structServerTest()
			throws Exception {
		String usedItfs[] = {"common.Struct"};
		compileADL("common.StructServer",usedItfs);
	}

	/**
	 * Clent interface with struct definition.
	 */
	@Test(groups = {"simpleGeneration"})
	public void structClientTest()
			throws Exception {
		String usedItfs[] = {"common.Struct"};
		compileADL("common.StructClient",usedItfs);
	}

	private void compileADL(String adlName, String usedItfs[])
			throws Exception {

		initPath();

		initContext(true);

		List<String> flags = new ArrayList<String>();

		final Definition d = runner.load(adlName);

		runner.addCFlags(flags);

		runner.compile(adlName, "plop");

		// Testing .adl.h file existence.
		File adlOutputFile = runner.outputFileLocator.getCSourceOutputFile(PathHelper.fullyQualifiedNameToPath(adlName, ADL_EXT), runner.context);
		assertTrue(adlOutputFile.exists());

		// Testing .itf.h existence.
		for (String itf : usedItfs) {
			File itfOutputFile = runner.outputFileLocator.getCSourceOutputFile(PathHelper.fullyQualifiedNameToPath(itf,ITF_EXT), runner.context);
			assertTrue(itfOutputFile.exists());
		}

		// Testing .make existence.
		File makeOutputFile = runner.outputFileLocator.getCSourceOutputFile(PathHelper.fullyQualifiedNameToPath(adlName, MAKE_EXT), runner.context);
		assertTrue(makeOutputFile.exists());

		File dir = runner.outputFileLocator.getCSourceOutputDir(runner.context);
		final int r = Runtime.getRuntime().exec("make -f" + makeOutputFile.getAbsolutePath(), null, dir).waitFor();
		assertEquals(r, 0, "Unexpected return value");

	}

}

