package graphics.nim.volterra.texture;

import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_UNDEFINED;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_UNSUPPORTED;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.GL_STENCIL_ATTACHMENT;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import graphics.nim.volterra.util.Log;

public class FrameBuffer {
	private int width, height;
	
	private int handle;
	
	private List<Texture> colorTextures = new ArrayList<Texture>();
	private int depthTex = -1;
	
	public FrameBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		
		handle = glGenFramebuffers();
	}
	
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, handle);
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public Texture getColorTexture(int i) {
		return colorTextures.get(i);
	}
	
	public void addColorTexture(int internal, int format, int type) {
		Texture colorTex = TextureLoader.createTex(width, height, internal, format, type, Sampling.POINT, null);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + colorTextures.size(), colorTex.handle, 0);
		colorTextures.add(colorTex);
	}
	
	public void addDepthTexture(int internal) {
		depthTex = glGenRenderbuffers();
	    glBindRenderbuffer(GL_RENDERBUFFER, depthTex);
	    glRenderbufferStorage(GL_RENDERBUFFER, internal, width, height);
	}
	
	public void setClearColor(float red, float green, float blue, float alpha) {
		glClearColor(red, green, blue, alpha);
	}
	
	public void enableColor(int target) {
		glReadBuffer(target);
		glDrawBuffer(target);
	}
	
	public void disableColor() {
		glReadBuffer(GL_NONE);
		glDrawBuffer(GL_NONE);
	}
	
	public void drawBuffers() {
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(colorTextures.size());
		for (int i = 0; i < colorTextures.size(); i++) {
			drawBuffers.put(GL_COLOR_ATTACHMENT0 + i);
		}
		drawBuffers.flip();
		
		glDrawBuffers(drawBuffers);
		
		if (depthTex != -1) {
		    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthTex);
		    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_STENCIL_ATTACHMENT, GL_RENDERBUFFER, depthTex);
		}
	}
	
	public void validate() {
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			int error = glCheckFramebufferStatus(GL_FRAMEBUFFER);
			
			String errorMessage = null;
			
			switch (error) {
			case GL_FRAMEBUFFER_UNDEFINED:
				errorMessage = "Target is the default framebuffer, but the default framebuffer does not exist"; break;
			case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				errorMessage = "Any of the framebuffer attachment points are framebuffer incomplete"; break;
			case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				errorMessage = "The framebuffer does not have any texture attached to it"; break;
			case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
				errorMessage = "The value of GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE "
							 + "for any color attachment point(s) named by GL_DRAW_BUFFERi"; break;
			case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
				errorMessage = "GL_READ_BUFFER is not GL_NONE and the value of GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE "
							 + "is GL_NONE for the color attachment point named by GL_READ_BUFFER"; break;
			case GL_FRAMEBUFFER_UNSUPPORTED:
				errorMessage = "The combination of internal formats of the attached textures violates "
							 + "an implementation-dependent set of restrictions."; break;
			case GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE:
				errorMessage = "The value of GL_RENDERBUFFER_SAMPLES is not the same for all attached renderbuffers"; break;
			default:
				errorMessage = "There is a problem with the framebuffer";
			}
			
			Log.error(errorMessage);
		}
	}
}
