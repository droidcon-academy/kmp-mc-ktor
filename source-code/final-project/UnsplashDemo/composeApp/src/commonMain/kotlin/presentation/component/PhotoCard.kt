package presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import domain.Photo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import unsplashdemo.composeapp.generated.resources.Res
import unsplashdemo.composeapp.generated.resources.preview

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PhotoCard(
    photo: Photo,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = 6.dp))
            .clickable { onClick(photo.urls.regular) }
            .padding(all = 12.dp)
    ) {
        Row {
            CoilImage(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                imageModel = { photo.user.image.small },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = photo.user.username)
        }
        Spacer(modifier = Modifier.height(10.dp))
        CoilImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(size = 6.dp)),
            imageModel = { photo.urls.regular },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(Res.drawable.preview),
                        contentDescription = "Preview image",
                        tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.5f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = photo.parseDate(),
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
            Text(
                text = "${photo.likes} Likes",
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.then(
                if (photo.description != null) Modifier.alpha(1f)
                else Modifier.alpha(0.5f)
            ),
            text = photo.description ?: "No description."
        )
    }
}