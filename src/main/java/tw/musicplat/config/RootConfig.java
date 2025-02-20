package tw.musicplat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RootConfig implements WebMvcConfigurer {
    @Value("${photo.storage.prefix}")
    private String photoPath;
    // 配置 Google Cloud Storage (GCS)
//    @Bean
//    public Storage googleStorage() {
//        return StorageOptions.getDefaultInstance().getService();  // 返回 Google Cloud Storage 服務實例
//    }

    // 處理所有以 /img 開頭的靜態資源請求
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 使用 photoPath 作為圖片的存儲路徑
        registry.addResourceHandler("/img/**") // 匹配所有 /img 開頭的 URL
                .addResourceLocations("file:" + photoPath); // 配置圖片路徑，將配置的 photoPath 作為資源位置
    }
}
