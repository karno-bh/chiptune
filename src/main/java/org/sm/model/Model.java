package org.sm.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sm.math.Vec3f;

public class Model {
	
	private final String fileName;
	private final List<Vec3f> vertecies = new ArrayList<>();
	private final List<Vec3f[]> faces = new ArrayList<>();
	
	public Model(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			throw new IllegalArgumentException("Provided file name is empty");
		}
		this.fileName = fileName;
		parse();
	}
	
	private void parse() {
		File f = new File(fileName);
		if (!f.exists() || !f.isFile()) {
			throw new IllegalArgumentException("File: \"" + fileName + "\" does not exist or not a file");
		}
		try(BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("v ")) {
					Vec3f vertex = parseVertex(line);
					vertecies.add(vertex);
				} else if (line.startsWith("f ")) {
					Vec3f[] face = parseFace(line);
					faces.add(face);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Cannot read file: " + fileName, e);
		} 
	}
	
	private Vec3f parseVertex(String vertexDef) {
		String[] vertexLine = vertexDef.split(" ");
		if (vertexLine.length < 4) {
			throw new RuntimeException("Vertex line is too small");
		}
		float[] vec3fData = new float[3];
		for (int i = 1; i < vertexLine.length; i++) {
			vec3fData[i - 1] = Float.parseFloat(vertexLine[i].trim());
		}
		return new Vec3f(vec3fData);
 	}
	
	private Vec3f[] parseFace(String faceDef) {
		String[] faceLine = faceDef.split(" ");
		if (faceLine.length < 4) {
			throw new RuntimeException("Face line is too small");
		}
		Vec3f[] faceVertices = new Vec3f[3];
		for (int i = 1; i < faceLine.length; i++) {
			String[] faceData = faceLine[i].split("/");
			int vertexIndex = Integer.valueOf(faceData[0].trim());
			Vec3f vertex = vertecies.get(vertexIndex - 1);
			faceVertices[i - 1] = vertex;
		}
		return faceVertices;
	}
	
	public List<Vec3f[]> getFaces() {
		return faces;
	}
	
	public static void main(String[] args) {
		test001();
	}
	
	static void test001() {
		String file = "c:/my/temp/african_head.obj";
		Model m = new Model(file);
		System.out.println(m.getFaces());
	}
}
