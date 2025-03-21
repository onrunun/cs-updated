#!/bin/bash

BASE_DIR="."

# .kt dosyalarını bul
find "$BASE_DIR" -type f -name "*.kt" | while read -r kt_file; do
    # mainUrl değerini bul
    old_url=$(grep -oP 'override\s+var\s+mainUrl\s*=\s*"\K[^"]+' "$kt_file")
    
    if [[ -n "$old_url" ]]; then
        echo "[~] Kontrol Ediliyor: $kt_file"
        
        # Yeni URL'yi almak için curl kullan (redirect sonucu alır)
        new_url=$(curl -Ls -o /dev/null -w "%{url_effective}" "$old_url")
        
        if [[ "$old_url" != "$new_url" ]]; then
            # Dosya içinde eski URL'yi yeni URL ile değiştir
            sed -i "s|$old_url|$new_url|g" "$kt_file"
            echo "[»] Güncellendi: $old_url -> $new_url"
        else
            echo "[+] Güncellemeye gerek yok: $old_url"
        fi
    fi
done
