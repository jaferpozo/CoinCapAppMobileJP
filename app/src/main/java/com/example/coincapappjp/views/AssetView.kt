package com.example.coincapappjp.views
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.coincapappjp.R
import com.example.coincapappjp.models.Asset

@Composable
fun  AssetRow(asset: Asset) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
       // Icon(
        //   imageVector = Icons.Filled.AccountCircle,
        // contentDescription = null,
        //    tint = Color.Red,
        //   modifier = Modifier.size(58.dp)
       // )
        AsyncImage(
            model = "https://assets.coincap.io/assets/icons/${asset.symbol.lowercase()}@2x.png",
            contentDescription = null,
            modifier = Modifier.size(48.dp),)


        Spacer(modifier = Modifier.width(25.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = asset.symbol,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = asset.name,
                fontSize = 14.sp
            )
        }

        Icon(



            imageVector = if (asset.percentage>1) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = if (asset.percentage>1) Color.Red else Color.Green,
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = asset.price,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AssetRowPreview(){
Column (
    verticalArrangement = Arrangement.Center,
    modifier= Modifier.fillMaxSize()
) {     AssetRow(
    asset = Asset(
        id = "1",
        name = "Ethereun",
        price = "$10000",
        symbol ="ETH",
        percentage = 2.00,
    )

)
    AssetRow(
        asset = Asset(
            id = "2",
            name = "BTC",
            price = "$8000",
            symbol ="Bitcoin",
            percentage = 0.00,
        )

    )
}

}
