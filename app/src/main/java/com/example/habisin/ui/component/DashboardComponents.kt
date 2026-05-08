package com.example.habisin.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habisin.ui.model.ProductModel
import com.example.habisin.ui.theme.HabisinCoral
import com.example.habisin.ui.theme.HabisinLime
import com.example.habisin.ui.theme.HabisinOlive
import com.example.habisin.ui.theme.HabisinPeach
import com.example.habisin.ui.theme.HabisinTextDark

@Composable
fun ExpiringSoonCard(
    items: List<ProductModel>,
    onItemClick: (ProductModel) -> Unit,
    onBellClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(HabisinCoral)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "ATTENTION REQUIRED",
                    color      = HabisinTextDark,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text       = if (items.isEmpty()) "Nothing expiring soon"
                    else "${items.size} items expiring soon",
                    color      = HabisinTextDark,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(HabisinPeach)
                    .clickable(onClick = onBellClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.NotificationsActive,
                    contentDescription = "Notifications",
                    tint               = HabisinCoral,
                    modifier           = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        if (items.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(HabisinPeach)
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "No food yet, try adding some",
                    color      = HabisinTextDark,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            items.forEach { item ->
                ExpiringItemRow(item = item, onClick = { onItemClick(item) })
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ExpiringItemRow(item: ProductModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(HabisinPeach)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image placeholder (ganti pake Coil/Image kalau udah ada)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.6f))
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = item.name,
                color      = HabisinTextDark,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text       = "${item.daysLeft} DAY${if (item.daysLeft == 1) "" else "S"} LEFT",
                color      = HabisinTextDark.copy(alpha = 0.7f),
                fontSize   = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }

        Icon(
            imageVector        = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint               = HabisinTextDark
        )
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(HabisinLime)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = iconTint,
                modifier           = Modifier.size(20.dp)
            )
        }

        Text(
            text       = value,
            color      = HabisinOlive,
            fontSize   = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text       = label,
            color      = HabisinOlive,
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text       = title,
            color      = HabisinTextDark,
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold
        )
        if (actionLabel != null) {
            Text(
                text       = actionLabel,
                color      = HabisinOlive,
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}