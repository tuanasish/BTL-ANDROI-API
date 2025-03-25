package transport

import (
	"BOOKING_BE/modules/field/biz"
	"BOOKING_BE/modules/field/storage"
	"context"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func DeleteField(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Lấy id từ param
		id, err := strconv.Atoi(c.Param("id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "ID không hợp lệ"})
			return
		}

		// Khởi tạo storage và biz
		fieldStorage := storage.NewStorageField(db)
		fieldBiz := biz.NewDeleteFieldBiz(fieldStorage)

		// Thực hiện xoá
		if err := fieldBiz.DeleteField(context.Background(), id); err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusOK, gin.H{"message": "Field deleted successfully"})
	}
}
