package des.wangku.operate;

import org.apache.log4j.Logger;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * ASM读取Class文件流
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class MQClassVisitor implements ClassVisitor, Opcodes {
	static Logger logger = Logger.getLogger(MQClassVisitor.class);
	String classFile = null;

	@Override
	public void visit(int arg0, int arg1, String arg2, String arg3, String arg4, String[] arg5) {
		//logger.debug("visit:arg0:" + arg0 + "\t" + "arg1:" + arg1 + "\t" + "arg2:" + arg2 + "\t" + "arg3:" + arg3 + "\t" + "arg4:" + arg4 + "\t");
		if (Const.ACC_StandardTaskClass.equals(arg4)) classFile = arg2.replace('/', '.');
	}

	@Override
	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		//logger.debug("visitAnnotation:arg0:"+arg0+"\t"+"arg1:"+arg1+"\t");
		return null;
	}

	@Override
	public void visitAttribute(Attribute arg0) {

	}

	@Override
	public void visitEnd() {

	}

	@Override
	public FieldVisitor visitField(int arg0, String arg1, String arg2, String arg3, Object arg4) {
		return null;
	}

	@Override
	public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {

	}

	boolean isStructure1 = false;
	boolean isStructure2 = false;
	static final String initArg1 = "<init>";
	static final String paraArg2_1 = "(Lorg/eclipse/swt/widgets/Composite;)V";
	static final String paraArg2_2 = "(Lorg/eclipse/swt/widgets/Composite;I)V";

	@Override
	public MethodVisitor visitMethod(int arg0, String arg1, String arg2, String arg3, String[] arg4) {
		//logger.debug("visit:arg0:" + arg0 + "\t" + "arg1:" + arg1 + "\t" + "arg2:" + arg2 + "\t" + "arg3:" + arg3 + "\t" + "arg4:" + arg4 + "\t");
		if (!initArg1.equals(arg1)) return null;
		if (paraArg2_1.equals(arg2)) isStructure1 = true;
		if (paraArg2_2.equals(arg2)) isStructure2 = true;
		return null;
	}

	@Override
	public void visitOuterClass(String arg0, String arg1, String arg2) {
		//logger.debug("visitOuterClass :arg0:"+arg0+"\t"+"arg1:"+arg1+"\t"+"arg2:"+arg2+"\t");

	}

	@Override
	public void visitSource(String arg0, String arg1) {
		//logger.debug("visitSource :arg0:"+arg0+"\t"+"arg1:"+arg1+"\t");

	}

}
