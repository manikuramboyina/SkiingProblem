package com.Redmart.SkiingInSingapore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mani
 *
 */
public class Elevation {
int elevation;
int maxDepth ;
List<Elevation> AdjSteepNodes;
int endingElevation;
boolean visited;
LinkedList<Elevation> deepestElevationPath;
Elevation nextDeepest;

public Elevation(int elevation) {
	this.elevation = elevation;
	this.endingElevation = elevation;
	this.maxDepth = 0;
	this.visited = false;
	this.AdjSteepNodes = new ArrayList<Elevation>();
	this.deepestElevationPath = new LinkedList<Elevation>();
	
}

}