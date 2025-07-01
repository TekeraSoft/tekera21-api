ALTER TABLE variations
    ADD COLUMN position INTEGER NOT NULL DEFAULT 0;

-- 2) Mevcut her ürün için pozisyonu doldur
--    Burada created_at + id sırasına göre numaralandırıyoruz.
WITH ordered AS (
    SELECT id,
           ROW_NUMBER() OVER (PARTITION BY product_id
               ORDER BY created_at NULLS LAST, id) - 1 AS rn
    FROM   variations
)
UPDATE variations v
SET    position = o.rn
FROM   ordered o
WHERE  v.id = o.id;
