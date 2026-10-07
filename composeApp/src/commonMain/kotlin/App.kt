import androidx.compose.runtime.Composable
import org.koin.compose.KoinContext
import org.lemb.tasktrack.TaskTrackApp
import org.lemb.tasktrack.ui.theme.TaskTrack

@Composable
fun App() {
    KoinContext {
        TaskTrack {
            TaskTrackApp()
        }
    }
}
