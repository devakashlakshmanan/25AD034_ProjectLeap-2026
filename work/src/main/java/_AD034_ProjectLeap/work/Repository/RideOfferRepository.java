package _AD034_ProjectLeap.work.Repository;

import _AD034_ProjectLeap.work.Models.RideOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideOfferRepository extends JpaRepository<RideOffer,Long> {
}
