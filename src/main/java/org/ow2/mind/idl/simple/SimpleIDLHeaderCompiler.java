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
 * Authors: Stephane Seyvoz
 * Contributors: 
 */

package org.ow2.mind.idl.simple;
		
import static org.ow2.mind.SourceFileWriter.writeToFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.CompilerError;
import org.objectweb.fractal.adl.util.FractalADLLogManager;
import org.ow2.mind.InputResourceLocator;
import org.ow2.mind.InputResourcesHelper;
import org.ow2.mind.PathHelper;
import org.ow2.mind.SimpleMacroHelper;
import org.ow2.mind.adl.simple.SimpleADLDefinitionSourceGenerator;
import org.ow2.mind.idl.IDLLoader;
import org.ow2.mind.idl.IDLVisitor;
import org.ow2.mind.idl.ast.IDL;
import org.ow2.mind.idl.ast.IDLASTHelper;
import org.ow2.mind.idl.ast.Include;
import org.ow2.mind.idl.ast.IncludeContainer;
import org.ow2.mind.idl.ast.InterfaceDefinition;
import org.ow2.mind.idl.ast.Method;
import org.ow2.mind.idl.ast.Parameter;
import org.ow2.mind.idl.ast.Type;
import org.ow2.mind.idl.ast.TypeCollectionContainer;
import org.ow2.mind.idl.ast.TypeDefinition;
import org.ow2.mind.io.IOErrors;
import org.ow2.mind.io.OutputFileLocator;
import org.ow2.mind.st.AbstractStringTemplateProcessor;
import org.ow2.mind.st.BackendFormatRenderer;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Inspired by @see IDLHeaderCompiler.
 * @author sesa231795
 *
 */
public class SimpleIDLHeaderCompiler extends AbstractStringTemplateProcessor implements IDLVisitor {

	/** The name to be used to inject the templateGroupName used by this class. */
	public static final String     TEMPLATE_NAME    = "SimpleIDL";

	/** The default templateGroupName used by this class. */
	public static final String     DEFAULT_TEMPLATE = "st.interfaces.simple.IDL2C";

	protected final static String  IDT_FILE_EXT     = "idt.h";
	protected final static String  ITF_FILE_EXT     = "itf.h";

	protected static Logger        depLogger        = FractalADLLogManager.getLogger("dep");

	protected static Logger logger = FractalADLLogManager.getLogger("simple-gen");
	
	@Inject
	protected OutputFileLocator    outputFileLocatorItf;
	
	@Inject
	IDLLoader idlLoaderItf;
	
	//To relaunch IDL treatment on parent IDL
	@Inject
	IDLVisitor idlVisitorItf; 
	
	@Inject
	protected InputResourceLocator inputResourceLocatorItf;

	@Inject
	protected SimpleIDLHeaderCompiler(@Named(TEMPLATE_NAME) final String templateGroupName) {
		super(templateGroupName);
	}

	// ---------------------------------------------------------------------------
	// Implementation of the Visitor interface
	// ---------------------------------------------------------------------------
	
	// TODO: Use StringTemplate + regenerate method (incremental compilation)
	public void visit(final IDL idl, final Map<Object, Object> context)
			throws ADLException {

		// Ignore include headers (.idt etc)
		final String headerFileName;
		if (idl.getName().startsWith("/")) {
			// IDT file. (Don't really understand this test)
			// Anyway I don't treat .idt files.
			// headerFileName = PathHelper.replaceExtension(idl.getName(), IDT_FILE_EXT);
			return;
		} else {
			headerFileName = PathHelper.fullyQualifiedNameToPath(idl.getName(),ITF_FILE_EXT);    
			final File headerFile = outputFileLocatorItf.getCSourceOutputFile(
			        headerFileName, context);
		    if (regenerate(headerFile, idl, context)) {
		      final StringTemplate st = getInstanceOf("idlFile");

		      st.setAttribute("idl", idl);
		      try {
		        writeToFile(headerFile, st.toString());
		      } catch (final IOException e) {
		        throw new CompilerError(IOErrors.WRITE_ERROR, e,
		            headerFile.getAbsolutePath());
		      }
		    }
		}
		
		
		
//		try {
//			// Creating a File and a PrintWriter to write in it
//			final File headerFile = outputFileLocatorItf.getCSourceOutputFile(
//					PathHelper.fullyQualifiedNameToPath(idl.getName(), "itf.println.h"), context);
//			PrintWriter itfWriter = new PrintWriter(headerFile,"ASCII");
//			
//			SimpleMacroHelper.openIncludeGuard(idl.getName(), itfWriter);
//			
//			
//			// interface inheritance and ImportIDL.
//			try {
//				if (idl instanceof InterfaceDefinition) {
//					itfWriter.println("/* Begin includes referenced interface */");
//					for (InterfaceDefinition itfDef : IDLASTHelper.getReferencedInterfaces(idl,idlLoaderItf,context)){
//						IDL subIdl = idlLoaderItf.load(itfDef.getName(), context);
//						itfWriter.println("#include \"" + SimpleMacroHelper.rmLeadingSlash(PathHelper.fullyQualifiedNameToPath(itfDef.getName(), "itf.h")) + "\"");
//						idlVisitorItf.visit(subIdl, context);
//					}
//					itfWriter.println("/* End includes referenced interface */");
//				}
//			} catch (ADLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	
//			
//			// Propagating the include directive
//			if (idl instanceof IncludeContainer) {
//				Include[] includes = ((IncludeContainer)idl).getIncludes();
//				if ((includes != null) && (includes.length !=0)){
//					itfWriter.println();
//					itfWriter.println("/* Begin includes imported from interface */");
//					for (Include include : includes) {
//						String incPath = include.getPath();
//						//Strip the leading annoying / from the include
//						if (incPath.startsWith("\"")) incPath = "\"" + SimpleMacroHelper.rmLeadingSlash(incPath.substring(1));
//						itfWriter.println("#include " + incPath );
//					}
//					itfWriter.println("/* End includes imported from interface */");
//					itfWriter.println();
//				}
//			}	
//
//			// propagating types defined directly in the .itf
//			if (idl instanceof TypeCollectionContainer) {
//				for (final Type type : ((TypeCollectionContainer) idl).getTypes()) {
//					if (type instanceof TypeDefinition)
//						itfWriter.println("typedef " + SimpleMacroHelper.typeToString(((TypeDefinition)type).getType()) + " " + SimpleMacroHelper.typeToString(type) + ";");
//				}
//			}
//
//			// define a struct and a typedef for the interface.
//			if (idl instanceof InterfaceDefinition) {
//				Method[] meths = ((InterfaceDefinition)idl).getMethods();
//				if ((meths != null) && (meths.length !=0)){
//					itfWriter.println();
//					itfWriter.println("/* Begin interface type definition */");
//					itfWriter.println("struct " + idl2type(idl) + "_s {");
//					for (Method meth : meths) {
//						itfWriter.print("\t" + SimpleMacroHelper.typeToString(meth.getType()) + " (*" + meth.getName() + ")(" );
//						Parameter[] parameters = meth.getParameters();
//						SimpleADLDefinitionSourceGenerator.writeCFunctionParameters(parameters,itfWriter);
//						itfWriter.println(");");
//					}
//					itfWriter.println("};" );
//					itfWriter.println();
//					itfWriter.println("typedef struct " + idl2type(idl) + "_s " + idl2type(idl) + ";");
//					itfWriter.println("/* End interface type definition */");
//					itfWriter.println();
//				}
//			}
//			// Close the interface header file.
//			SimpleMacroHelper.closeIncludeGuard(idl.getName(), itfWriter);
//			itfWriter.close();
//
//		} catch (FileNotFoundException e) {
//			logger.info("Somehow calculated file path are wrong this is a BUG  !");
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e1) {
//			logger.info("ASCII encoding is not supported on your platform !");
//			e1.printStackTrace();
//		}

	}
	
	private boolean regenerate(final File outputFile, final IDL idl,
			final Map<Object, Object> context) {
		if (!outputFile.exists()) {
			if (depLogger.isLoggable(Level.FINE)) {
				depLogger.fine("Generated source file '" + outputFile
						+ "' does not exist, generate.");
			}
			return true;
		}

		if (!inputResourceLocatorItf.isUpToDate(outputFile,
				InputResourcesHelper.getInputResources(idl), context)) {
			if (depLogger.isLoggable(Level.FINE)) {
				depLogger.fine("Generated source file '" + outputFile
						+ "' is out-of-date, regenerate.");
			}
			return true;
		} else {
			if (depLogger.isLoggable(Level.FINE)) {
				depLogger.fine("Generated source file '" + outputFile
						+ "' is up-to-date, do not regenerate.");
			}
			return false;
		}
	}

	@Override
	protected void registerCustomRenderer(final StringTemplateGroup templateGroup) {
		templateGroup.registerRenderer(String.class, new BackendFormatRenderer() {
			@Override
			public String toString(final Object o, final String formatName) {
				if ("toIncludePath".equals(formatName)) {
					final String s = o.toString();
					String path = s.substring(1, s.length() - 1);
//					if (path.endsWith(".idt")) {
//						path += ".h";
//					}
					if (path.startsWith("/")) {
						path = path.substring(1);
					}
					return s.substring(0, 1) + path + s.substring(s.length() - 1);
				} else {
					return super.toString(o, formatName);
				}
			}
		});
	}
	/**
	 * get a C type from an interface instance. 
	 * @param idl The interface.
	 * @return The C type name.
	 */
	public String idl2type(IDL idl) {
		return idl.getName().replace(".", "_");
	}
}
