package com.example.udemycourseshoppingapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.aditya.measuremate.R
import com.example.udemycourseshoppingapp.common.utils.helper.MyLogger

@Composable
fun ImageLoading(
    imageUrl: String,
    modifier: Modifier = Modifier.size(80.dp),
    contentScale: ContentScale = ContentScale.FillWidth
) {
    val context = LocalContext.current
    val model = ImageRequest.Builder(context)
        .data(imageUrl)
        .crossfade(true)
        .size(Size.ORIGINAL)
        .build()


    val imageState =
        rememberAsyncImagePainter(model = model, imageLoader = context.imageLoader).state


    Box(modifier.background(color = Color.White), contentAlignment = Alignment.Center) {

        when (imageState) {
            is AsyncImagePainter.State.Loading -> {
                // Show loading animation
                LoadingAnimation()
            }

            is AsyncImagePainter.State.Error -> {
                // Show error UI
                val errorMessage = imageState.result.throwable.localizedMessage
                MyLogger.e(msg = "Failed to load image: ${imageState.result.throwable.cause?.message}")
                Text(
                    text = "Failed to load: $errorMessage",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            is AsyncImagePainter.State.Success -> {
                // Show loaded image
                Image(
                    painter = imageState.painter,
                    contentDescription = "Loaded Image",
                    contentScale = contentScale,
                    modifier = modifier
                )
            }

            else -> {
                // Handle other states if necessary
            }
        }
    }

}

@Composable
fun CircleImageLoading(
    imageUrl: String?,
    modifier: Modifier = Modifier.size(80.dp),
    contentScale: ContentScale = ContentScale.FillWidth
) {
    val context = LocalContext.current


    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .error(R.drawable.app_logo)
            .build(), imageLoader = context.imageLoader
    ).state


    Box(modifier.background(color = Color.Transparent), contentAlignment = Alignment.Center) {

        when (imageState) {
            is AsyncImagePainter.State.Loading -> {
                // Show loading animation
                LoadingAnimation()
            }

            is AsyncImagePainter.State.Error -> {
                // Show error UI
                val errorMessage = imageState.result.throwable.localizedMessage
                MyLogger.e(msg = "Failed to load image: ${imageState.result.throwable.cause?.message}")
//                Text(
//                    text = "Failed to load: $errorMessage",
//                    color = Color.Red,
//                    style = MaterialTheme.typography.bodyMedium
//                )

                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = "Load Image",
                    contentScale = contentScale,
                    modifier = Modifier
                        .clip(CircleShape),

                    )
            }

            is AsyncImagePainter.State.Success -> {
                // Show loaded image
                Image(
                    painter = imageState.painter,
                    contentDescription = "Load Image",
                    contentScale = contentScale,
                    modifier = Modifier
                        .clip(CircleShape),

                    )
            }

            else -> {
                // Handle other states if necessary
                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = "Load Image",
                    contentScale = contentScale,
                    modifier = Modifier
                        .clip(CircleShape),

                    )
            }
        }
    }


}


@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    dotCount: Int = 3,
    dotSize: Dp = 8.dp,
    spaceBetween: Dp = 4.dp,
    bounceHeight: Dp = 16.dp,
    animationDelay: Int = 300,
    text: String = "Loading..."
) {
    val infiniteTransition = rememberInfiniteTransition()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(dotCount) { index ->
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -bounceHeight.value,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        if (index == 0) animationDelay else index * animationDelay / dotCount,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse,
                ), label = ""
            )
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .offset(y = offsetY.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
        AddHorizontalSpace(8)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}