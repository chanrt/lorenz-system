float rho = 28;
float sigma = 10;
float beta = 8.0 / 3.0;

import peasy.*;  
PeasyCam camera;

ArrayList<PVector> points = new ArrayList<PVector>();
float hue_start;
int steps_per_frame = 10;

int generateRequest, chunkSize = 100;

float x = 1, y = 2, z = 3;
float dx, dy, dz;
float prec;

boolean paused;

void setup() {
  fullScreen(P3D);
  colorMode(HSB);

  camera = new PeasyCam(this, 500);
  prec = 0.005;
  hue_start = random(255);

  textSize(20);
  textAlign(CENTER);
}

void draw() {

  if (!paused) {
    for (int cycle = 0; cycle < steps_per_frame; cycle++) {
      dx = sigma * (y - x) * prec;
      dy = x * (rho - z) * prec;
      dz = (x * y - beta * z) * prec;

      x += dx;
      y += dy;
      z += dz;
      points.add(new PVector(x, y, z));
    }
  }

  while (--generateRequest > 0) {
    dx = sigma * (y - x) * prec;
    dy = x * (rho - z) * prec;
    dz = (x * y - beta * z) * prec;

    x += dx;
    y += dy;
    z += dz;
    points.add(new PVector(x, y, z));
  }

  float hue = hue_start;

  background(0);
  translate(0, 0, -80);
  scale(4);
  noFill();
  beginShape();
  for (PVector point : points) {
    stroke(hue, 255, 255);
    vertex(point.x, point.y, point.z);
    hue += 0.1;

    if (hue > 255) {
      hue = 0;
    }
  }
  endShape();

  text("Rho: " + Float.toString(rho), -width/4, -height/4, -500);
  text("Sigma: " + Float.toString(sigma), 0, -height/4, -500);
  text("Beta: " + Float.toString(beta), width/4, -height/4, -500);
  text("Generation rate: " + Integer.toString(steps_per_frame), 0, height / 4, -500);
}

void keyPressed() {
  if (key == 'p' || key == 'P') {
    paused = paused ? false : true;
  }
  if (key == 'c' || key == 'C') {
    camera.reset(1000);
  }
  if (key == 'r' || key == 'R') {
    x = 1;
    y = 2;
    z = 3;
    points.clear();
    hue_start = random(255);
  }

  if (keyCode == RIGHT) {
    generateRequest = chunkSize;
  }

  if (keyCode == UP) {
    steps_per_frame += 5;
  }
  if (keyCode == DOWN && steps_per_frame >= 5) {
    steps_per_frame -= 5;
  }

  if (key == 'q' || key == 'Q') {
    rho -= 1;
  }
  if (key == 'w' || key == 'W') {
    rho += 1;
  }

  if (key == 'a' || key == 'A') {
    sigma -= 1;
  }
  if (key == 's' || key == 'S') {
    sigma += 1;
  }

  if (key == 'z' || key == 'Z') {
    beta -= 1;
  }
  if (key == 'x' || key == 'X') {
    beta += 1;
  }
}
