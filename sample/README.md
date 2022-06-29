# [simprokcore](https://github.com/simprok-dev/simprokcore-android) sample

## Introduction

This sample is created to showcase the main features of the framework. In order to demonstrate the simplicity of it comparing to the basic example, we are making the same [sample](https://github.com/simprok-dev/simprokmachine-android/tree/main/sample) as in ```simprokmachine```.


The sample is divided into 10 easy steps demonstrating the flow of the app development and API usage.


## Step 0 - Describe application's behavior

Let's assume we want to create a counter app that shows a number on the screen and logcat each time it is incremented. 

When we reopen the app we want to see the same number. So the state must be saved in persistent storage. 


## Step 1 - Code application's state and event

Here is our global state of the application.

```Kotlin
data class AppState(val value: Int)
```

Here are our events of the application.

```Kotlin
sealed interface AppEvent {

    object UILayerEvent : AppEvent

    data class StorageLayerEvent(val value: Int) : AppEvent
}
```

## Step 2 - List down APIs

Here is our APIs we are going to use.

- ```Android Fragments```
- ```SharedPreferences```
- ```println()```

Each API is going to be our layer.

## Step 3 - Code UI layer

- State:

```Kotlin
data class UILayerState(val text: String)
```

- Event:

```Kotlin
object UILayerEvent
```

- Machine hierarchy:

Will be provided by [start()](https://github.com/simprok-dev/simprokandroid/wiki/AppCompatActivityExt#start-without-renderer) method's callback.



- Layer class:


```Kotlin
class UILayer(
    override val machine: WidgetMachine<UILayerState, UILayerEvent>
) : WidgetLayer.Type<AppState, AppEvent, UILayerState, UILayerEvent> {

    override fun mapState(state: AppState): UILayerState = UILayerState("${state.value}")

    override fun mapEvent(event: UILayerEvent): AppEvent = AppEvent.UILayerEvent
}
```

## Step 4 - Code storage layer

- State:

```Kotlin
data class StorageLayerState(val value: Int)
```

- Event:

```Kotlin
data class StorageLayerEvent(val value: Int)
```

- Machine hierarchy:

```Kotlin
class StorageLayerMachine(
    private val prefs: SharedPreferences
) : ChildMachine<StorageLayerState, StorageLayerEvent> {

    private val key = "storage"

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun process(input: StorageLayerState?, callback: Handler<StorageLayerEvent>) {
        if (input != null) {
            prefs.edit().putInt(key, input.value).apply()
        } else {
            callback(StorageLayerEvent(prefs.getInt(key, 0)))
        }
    }
}
```

- Layer class:

```Kotlin
class StorageLayer(
    private val prefs: SharedPreferences
) : MachineLayerType<AppState, AppEvent, StorageLayerState, StorageLayerEvent> {

    override val machine: Machine<StorageLayerState, StorageLayerEvent>
        get() = StorageLayerMachine(prefs)

    override fun mapState(state: AppState): StorageLayerState = StorageLayerState(state.value)

    override fun mapEvent(event: StorageLayerEvent): AppEvent = AppEvent.StorageLayerEvent(event.value)
}
```

## Step 6 - Code Logger layer

- State is going to be ```String```.

- Event is going to be ```Nothing``` as we don't send any events.

- Logger machine:

```Kotlin
class LoggerMachine: ChildMachine<String, Nothing> {

    override val dispatcher: CoroutineDispatcher
        get() = IO

    override fun process(input: String?, callback: Handler<Nothing>) {
        println(input ?: "loading")
    }
}
```

- Layer class:

```Kotlin
class LoggerLayer : ConsumerLayerType<AppState, AppEvent, String, Nothing> {

    override val machine: Machine<String, Nothing>
        get() = LoggerMachine()

    override fun map(state: AppState): String = "${state.value}"
}
```
    
## Step 7 - Code MainActivity
    
Now when we have all layers prepared we must connect them together. To do this, we need to create ```MainActivity```.

```Kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

## Step 8 - Run the flow!

Add fragment and start the flow with ```CoreAssembly```.


```Kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	...

	if (savedInstanceState == null) {
		supportFragmentManager.beginTransaction()
		    .add(R.id.container, MainFragment(), "tag")
                    .commit()

		start<UILayerState, UILayerEvent> {
			CoreAssembly.create(
			    UILayer(it),
			    LoggerLayer(),
			    StorageLayer(getSharedPreferences("storage", MODE_PRIVATE))
			) { state, event ->
			    if (state != null) {
				when (event) {
				    is AppEvent.UILayerEvent -> ReducerResult.Set(AppState(state.value + 1))
				    is AppEvent.StorageLayerEvent -> ReducerResult.Skip()
				}
			    } else {
				when (event) {
				    is AppEvent.UILayerEvent -> ReducerResult.Skip()
				    is AppEvent.StorageLayerEvent -> ReducerResult.Set(AppState(event.value))
				}
			    }
			}
                }
        }
}
```

## Step 9 - Enjoy yourself once again

Run the app and see how things are working.


![result](https://github.com/simprok-dev/simprokcore-android/blob/main/sample/images/results.gif)


## To sum up

As you can see this template is way simpler and more straightforward than using a ```simprokmachine``` for your architectural design.
