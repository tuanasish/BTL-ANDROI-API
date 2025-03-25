package storage

import "gorm.io/gorm"

type StorageField struct {
	db *gorm.DB
}

func NewStorageField(db *gorm.DB) *StorageField {
	return &StorageField{db: db}
}
