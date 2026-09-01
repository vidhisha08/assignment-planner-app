package com.example.studybuddy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studybuddy.ui.theme.*
import com.example.studybuddy.viewmodel.AuthViewModel


@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()


    var isLoginTab by remember { mutableStateOf(true) }


    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.clearSuccess()
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Lavender50)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Lavender300)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))


            Text(text = "📚", fontSize = 52.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "StudyBuddy",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Lavender800
            )
            Text(
                text = "Your Assignment Planner",
                fontSize = 13.sp,
                color = Lavender600,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Lavender50, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        TabButton(
                            text = "log in",
                            selected = isLoginTab,
                            onClick = { isLoginTab = true; viewModel.onEmailChange("") }
                        )
                        TabButton(
                            text = "register",
                            selected = !isLoginTab,
                            onClick = { isLoginTab = false; viewModel.onEmailChange("") }
                        )
                    }

                    Spacer(Modifier.height(20.dp))


                    OutlinedTextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("email address") },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = Lavender300) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = pastelTextFieldColors()
                    )

                    Spacer(Modifier.height(12.dp))


                    OutlinedTextField(
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Lavender300) },
                        trailingIcon = {
                            IconButton(onClick = viewModel::togglePasswordVisible) {
                                Icon(
                                    imageVector = if (state.passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "toggle password",
                                    tint = Lavender300
                                )
                            }
                        },
                        visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = pastelTextFieldColors()
                    )

                    // confirm password, only for registering
                    AnimatedVisibility(visible = !isLoginTab) {
                        Column {
                            Spacer(Modifier.height(12.dp))
                            OutlinedTextField(
                                value = state.confirmPassword,
                                onValueChange = viewModel::onConfirmPasswordChange,
                                label = { Text("confirm password") },
                                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Lavender300) },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = pastelTextFieldColors()
                            )

                            // password strength bar
                            if (state.password.isNotEmpty()) {
                                Spacer(Modifier.height(8.dp))
                                PasswordStrengthBar(state.password)
                            }
                        }
                    }


                    AnimatedVisibility(visible = state.error != null) {
                        state.error?.let { msg ->
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = msg,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))


                    Button(
                        onClick = { if (isLoginTab) viewModel.login() else viewModel.register() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !state.isLoading,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Lavender300)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (isLoginTab) "log in" else "create account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Lavender800
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))


                    TextButton(onClick = { isLoginTab = !isLoginTab }) {
                        Text(
                            text = if (isLoginTab) "don't have an account? register"
                            else "already have an account? log in",
                            fontSize = 13.sp,
                            color = Lavender600
                        )
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}


@Composable
private fun RowScope.TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f).height(36.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Lavender300 else Color.Transparent,
            contentColor   = if (selected) Lavender800 else Lavender600
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp)
    ) {
        Text(text, fontSize = 13.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
    }
}


@Composable
private fun PasswordStrengthBar(password: String) {
    val strength = when {
        password.length >= 12 && password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() } -> 3
        password.length >= 8 -> 2
        else -> 1
    }
    val label = listOf("weak", "ok", "strong")[strength - 1]
    val color = listOf(MaterialTheme.colorScheme.error, Butter700, Mint400)[strength - 1]
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(3) { i ->
            Box(
                Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        if (i < strength) color else Lavender100,
                        RoundedCornerShape(2.dp)
                    )
            )
        }
        Text(label, fontSize = 11.sp, color = color, modifier = Modifier.width(40.dp), textAlign = TextAlign.End)
    }
}


@Composable
fun pastelTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = Lavender300,
    unfocusedBorderColor = Lavender100,
    focusedLabelColor    = Lavender600,
    unfocusedLabelColor  = Lavender300,
    cursorColor          = Lavender600
)
