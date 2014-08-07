package org.ow2.mind;

import java.io.PrintWriter;

import org.objectweb.fractal.adl.Definition;
import org.ow2.mind.adl.ast.MindInterface;
import org.ow2.mind.idl.ast.ArrayOf;
import org.ow2.mind.idl.ast.ConstantDefinition;
import org.ow2.mind.idl.ast.EnumDefinition;
import org.ow2.mind.idl.ast.EnumReference;
import org.ow2.mind.idl.ast.PointerOf;
import org.ow2.mind.idl.ast.PrimitiveType;
import org.ow2.mind.idl.ast.StructDefinition;
import org.ow2.mind.idl.ast.StructReference;
import org.ow2.mind.idl.ast.Type;
import org.ow2.mind.idl.ast.TypeDefReference;
import org.ow2.mind.idl.ast.TypeDefinition;
import org.ow2.mind.idl.ast.UnionDefinition;
import org.ow2.mind.idl.ast.UnionReference;

public class SimpleMacroHelper {

	static public String rmLeadingSlash(String path){
		if (path.startsWith("/")) path = path.substring(1);
		return path;
	}
	
	/**
	 * get a C type from an interface instance. 
	 * @param itf The interface.
	 * @return The C type name.
	 */
	public static String itf2type(MindInterface itf) {
		return itf.getSignature().replace(".", "_");
	}

	/**
	 * get a C type from a definition. 
	 * @param def The definition.
	 * @return The C type name.
	 */
	public static String def2type(Definition def) {
		return def.getName().replace(".", "_");
	}

	/**
	 * Typical C include guard header from fully qualified like Strings
	 * @param name The fully qualified name of the element to guard.
	 * @param writer The PrintWriter to write in.
	 */
	public static void openIncludeGuard(String name, PrintWriter writer) {
		final String macroName = name.replace(".", "_").toUpperCase();
		writer.println("#ifndef " + macroName);
		writer.println("#define " + macroName);
		writer.println();
	}

	/**
	 * Typical C include guard footer from fully qualified like Strings
	 * @param name The fully qualified name of the element to guard.
	 * @param writer The PrintWriter to write in.
	 */
	public static void closeIncludeGuard(String name, PrintWriter writer) {
		final String macroName = name.replace(".", "_").toUpperCase();
		writer.println();
		writer.println("#endif /* " + macroName + " */");
		writer.println();
	}

	/**
	 * Shamelessly copied from OptimCPLChecker
	 */
	public static String typeToString(Type type) {
		if (type instanceof EnumDefinition) {
			return ((EnumDefinition) type).getName();
		} else if (type instanceof EnumReference) {
			return ((EnumReference) type).getName();
		} else if (type instanceof StructDefinition) {
			return ((StructDefinition) type).getName();
		} else if (type instanceof StructReference) {
			return ((StructReference) type).getName();
		} else if (type instanceof UnionDefinition) {
			return ((UnionDefinition) type).getName();
		} else if (type instanceof UnionReference) {
			return ((UnionReference) type).getName();
		} else if (type instanceof TypeDefinition) {
			return ((TypeDefinition) type).getName();
		} else if (type instanceof TypeDefReference) {
			return ((TypeDefReference) type).getName();
		} else if (type instanceof ConstantDefinition) {
			return ((ConstantDefinition) type).getName();
		} else if (type instanceof PrimitiveType) {
			return ((PrimitiveType) type).getName();
		} else if (type instanceof ArrayOf) {
			// TODO:see IDL2C.stc arrayOfVarName for cleaner handling
			return typeToString(((ArrayOf) type).getType()) + " * "; 
		} else if (type instanceof PointerOf) {
			return typeToString(((PointerOf) type).getType()) + " * ";
		} else return ""; // TODO: check even if this should never happen, or raise an error
	}

	
}
