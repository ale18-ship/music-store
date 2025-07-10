package com.acerap.musicstore.domain.ports.in;

import com.acerap.musicstore.domain.model.CompactDisc;

import java.util.List;

public interface InventoryAlertService {
    List<CompactDisc> checkLowStock(int threshold);
}
