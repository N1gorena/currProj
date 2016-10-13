package com.guango.society;

import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glCreateProgram;

/**
 * Created by nijeguan on 10/8/16.
 */

public class WorldEngine implements GLSurfaceView.Renderer {
    private int programID;
    private  final String vertSrc = "#version 300 es\n"+
            "layout(location = 0) in vec3 position;\n"+
            "void main(){\n"+
            "   gl_Position = vec4(position,1.0);\n"+
            "}\n";
    private final String fragSrc = "#version 300 es\n"+
            "precision mediump float;\n"+
            "out vec4 arbitrary;\n"+
            "void main(){\n"+
            "   arbitrary = vec4(1.0f,0.0f,0.0f,1.0f);\n"+
            "}\n";
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        GLES31.glClearColor(0.66f,0.75f,0.33f,1.0f);
        GLES31.glEnable(GLES31.GL_DEPTH_TEST);

        programID = GLES31.glCreateProgram();
        int vertShader = GLES31.glCreateShader(GLES31.GL_VERTEX_SHADER);
        int fragShader = GLES31.glCreateShader(GLES31.GL_FRAGMENT_SHADER);
        int[] holster = new int[1];
        IntBuffer status = IntBuffer.wrap(holster);

        GLES31.glShaderSource(vertShader,vertSrc);
        GLES31.glShaderSource(fragShader,fragSrc);

        GLES31.glCompileShader(vertShader);
        GLES31.glGetShaderiv(vertShader,GLES31.GL_COMPILE_STATUS,status);
        if(status.get(0) != GLES31.GL_TRUE){
            Log.d( "SHADER:VERT:COMPILATION", GLES31.glGetShaderInfoLog(vertShader) );
            return;
        }

        GLES31.glCompileShader(fragShader);
        GLES31.glGetShaderiv(fragShader,GLES31.GL_COMPILE_STATUS,status);
        if(status.get(0) != GLES31.GL_TRUE){
            Log.d( "SHADER:FRAG:COMPILATION", GLES31.glGetShaderInfoLog(fragShader) );
            return;
        }

        GLES31.glAttachShader(programID,vertShader);
        GLES31.glAttachShader(programID,fragShader);

        GLES31.glLinkProgram(programID);
        GLES31.glGetProgramiv(programID,GLES31.GL_LINK_STATUS,status);
        if(status.get(0) != GLES31.GL_TRUE) {
            Log.d("PROGRAM:LINK", GLES31.glGetProgramInfoLog(programID));
        }

        GLES31.glDeleteShader(vertShader);
        GLES31.glDeleteShader(fragShader);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES31.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] Triangle = {
                -0.5f,0.0f,0.0f,
                0.5f,0.0f,0.0f,
                0.0f,0.5f,0.0f
        };
        ByteBuffer sysBuffer = ByteBuffer.allocateDirect(36);
        sysBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = sysBuffer.asFloatBuffer();
        floatBuffer.put(Triangle);
        floatBuffer.position(0);

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT | GLES31.GL_DEPTH_BUFFER_BIT);

        GLES31.glUseProgram(programID);
        GLES31.glEnableVertexAttribArray(0);
        GLES31.glVertexAttribPointer(0,3, GLES31.GL_FLOAT,false,0,floatBuffer);
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES,0,3);
        GLES31.glUseProgram(0);

    }
}
