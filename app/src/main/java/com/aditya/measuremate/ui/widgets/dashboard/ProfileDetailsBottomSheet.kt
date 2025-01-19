package com.aditya.measuremate.ui.widgets.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aditya.measuremate.domain.models.common.User
import com.aditya.measuremate.ui.theme.CustomBlue
import com.aditya.measuremate.ui.theme.CustomPink
import com.aditya.measuremate.ui.widgets.sign_in.GoogleSignInButton
import com.example.udemycourseshoppingapp.ui.components.AddVerticalSpace
import com.example.udemycourseshoppingapp.ui.components.CircleImageLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailsBottomSheet(
    modifier: Modifier = Modifier,
    isBottomSheetOpen: Boolean,
    user: User?,
    onBottomSheetDismiss: () -> Unit,
    primaryText: String,
    buttonLoadingState: Boolean,
    onGoogleButtonClick: () -> Unit

) {

    if (isBottomSheetOpen) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onBottomSheetDismiss,
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text("Profile", style = MaterialTheme.typography.titleLarge)
                    AddVerticalSpace(10)
                    HorizontalDivider()
                }
            }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircleImageLoading(
                    user?.profilePictureUrl ?: "", modifier = Modifier
                        .size(120.dp)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(listOf(CustomBlue, CustomPink)),
                            shape = CircleShape
                        )
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
                AddVerticalSpace(10)
                Text(if(user == null || user.isAnonymous) "Anonymous" else user.name , style = MaterialTheme.typography.bodyLarge )
                AddVerticalSpace(4)
                Text(if(user == null || user.isAnonymous) "anonymous@measuremate.io" else user.email , style = MaterialTheme.typography.bodySmall )
                AddVerticalSpace(20)
                GoogleSignInButton(
                    isLoading = buttonLoadingState ,
                    primaryText = primaryText ,
                    onClick = onGoogleButtonClick
                )
            }
        }
    }

}