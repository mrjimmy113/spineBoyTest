package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.esotericsoftware.spine.utils.SkeletonDataLoader;

public class MyGdxGame extends Game {

	PolygonSpriteBatch batch;
	OrthographicCamera camera;

	Stage stage;

	static AssetManager assetManager = new AssetManager();

	SkeletonActor actor;
	AnimationState.TrackEntry track0;
	AnimationState.TrackEntry track1;
	float alpha;

	@Override
	public void create () {
		batch = new PolygonSpriteBatch();
		camera = new OrthographicCamera();
		stage = new Stage(new ExtendViewport(1280,720, camera),batch);

		assetManager.setLoader(SkeletonData.class, new SkeletonDataLoader(new InternalFileHandleResolver()));
		loadSpine("spineboy", 1f);
		assetManager.finishLoading();

		SkeletonData skeletonData = assetManager.<SkeletonData>get(skelPath("spineboy"));

		actor = new SkeletonActor();
		SkeletonRenderer skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true);
		actor.setRenderer(skeletonRenderer);
		Skeleton skeleton = new Skeleton(skeletonData);
		actor.setSkeleton(skeleton);
		AnimationState state = new AnimationState(new AnimationStateData(skeletonData));
		actor.setAnimationState(state);
		actor.setX(400);

		stage.addActor(actor);
		track0 = state.setAnimation(0,"idle",true);
		track1 = state.setAnimation(1,"run",true);
		track1.setAlpha(0);

	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			alpha += Gdx.graphics.getDeltaTime();
		}else {
			alpha -= Gdx.graphics.getDeltaTime();
		}

		alpha = MathUtils.clamp(alpha,0,1);
		track1.setAlpha(alpha);

	}
	
	@Override
	public void dispose () {

	}


	private static String skelPath(String path) {
		return  path + ".skel";
	}
	private static String atlasPath(String path) {
		return path + ".atlas";
	}

	private static void loadSpine(String name, float scale) {
		managerLoadSpine(assetManager,name,atlasPath(name),scale);
	}

	public static void managerLoadSpineNoAtlas(AssetManager manager, String name, float scale) {
		manager.load(skelPath(name),SkeletonData.class,new SkeletonDataLoader.SkeletonDataParameter(atlasPath(name),scale));
	}

	public static void managerLoadSpine(AssetManager manager, String name, String atlas, float scale) {
		manager.load(skelPath(name),SkeletonData.class,new SkeletonDataLoader.SkeletonDataParameter(atlas,scale));
	}
}
