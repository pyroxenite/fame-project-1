all: SimpleRender.class

SimpleRender.class: SimpleRender.java Playground.class
	javac SimpleRender.java

Playground.class: Playground.java Vector.class Sprite.class Ball.class Rectangle.class Paddle.class Brick.class
	javac Playground.java

Paddle.class: Paddle.java Rectangle.class Vector.class
	javac Paddle.java

Brick.class: Brick.java Rectangle.class Vector.class
	javac Brick.java

Rectangle.class: Rectangle.java Sprite.class Vector.class
	javac Rectangle.java

Ball.class: Ball.java Sprite.class Vector.class
	javac Ball.java

Sprite.class: Sprite.java Vector.class
	javac Sprite.java

Vector.class: Vector.java
	javac Vector.java