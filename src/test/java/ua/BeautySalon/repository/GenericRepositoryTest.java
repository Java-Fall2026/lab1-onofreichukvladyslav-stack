package ua.BeautySalon.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.BeautySalon.exception.DuplicateIdentityException;
import ua.BeautySalon.model.BeautyClient;
import ua.BeautySalon.model.BeautyService;
import ua.BeautySalon.model.ServiceCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GenericRepository unit tests")
class GenericRepositoryTest {

    private BeautyClient sampleClient(String phone) {
        return BeautyClient.of(phone, "Test Client", LocalDate.of(1995, 5, 10));
    }

    @Test
    @DisplayName("Should store item with add and return it via findByIdentity")
    void testAddAndFindByIdentity() {
        GenericRepository<BeautyClient> repo = new GenericRepository<>("Clients", BeautyClient::phone);
        BeautyClient client = sampleClient("+380971112233");

        repo.add(client);
        Optional<BeautyClient> found = repo.findByIdentity("+380971112233");

        assertTrue(found.isPresent());
        assertEquals(client, found.get());
        assertEquals(1, repo.size());
    }

    @Test
    @DisplayName("Should return empty Optional when identity is not found")
    void testFindByIdentityMissingReturnsEmpty() {
        GenericRepository<BeautyClient> repo = new GenericRepository<>("Clients", BeautyClient::phone);

        Optional<BeautyClient> found = repo.findByIdentity("+380000000000");

        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should throw DuplicateIdentityException on duplicate add")
    void testDuplicateAddThrowsException() {
        GenericRepository<BeautyClient> repo = new GenericRepository<>("Clients", BeautyClient::phone);
        repo.add(sampleClient("+380971112233"));

        DuplicateIdentityException ex = assertThrows(
                DuplicateIdentityException.class,
                () -> repo.add(sampleClient("+380971112233"))
        );

        assertTrue(ex.getMessage().contains("+380971112233"));
        assertTrue(ex.getMessage().contains("Clients"));
    }

    @Test
    @DisplayName("Should return false and keep size unchanged when addIfAbsent sees duplicate")
    void testAddIfAbsentDuplicateReturnsFalse() {
        GenericRepository<BeautyClient> repo = new GenericRepository<>("Clients", BeautyClient::phone);
        repo.add(sampleClient("+380971112233"));

        boolean added = repo.addIfAbsent(sampleClient("+380971112233"));

        assertFalse(added);
        assertEquals(1, repo.size());
    }

    @Test
    @DisplayName("Should remove existing item and return false for missing identity")
    void testRemoveByIdentity() {
        GenericRepository<BeautyClient> repo = new GenericRepository<>("Clients", BeautyClient::phone);
        repo.add(sampleClient("+380971112233"));

        assertTrue(repo.removeByIdentity("+380971112233"));
        assertEquals(0, repo.size());
        assertFalse(repo.removeByIdentity("+380971112233"));
    }

    @Test
    @DisplayName("Should not allow mutating repository state through findAll list")
    void testFindAllImmutabilitySnapshot() {
        GenericRepository<BeautyClient> repo = new GenericRepository<>("Clients", BeautyClient::phone);
        repo.add(sampleClient("+380971112233"));

        List<BeautyClient> snapshot = repo.findAll();

        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(sampleClient("+380501112233")));
        assertEquals(1, repo.size());
    }

    @Test
    @DisplayName("Should work across distinct entity types including mutable classes")
    void testMultipleEntityTypes() {
        GenericRepository<BeautyClient> clientRepo = new GenericRepository<>("Clients", BeautyClient::phone);
        GenericRepository<BeautyService> serviceRepo = new GenericRepository<>("Services", BeautyService::getServiceCode);

        BeautyClient client = sampleClient("+380971112233");
        BeautyService service = new BeautyService("SRV-1", "Cut", ServiceCategory.HAIRCUT, 40, 500.0);

        clientRepo.add(client);
        serviceRepo.add(service);

        assertEquals(1, clientRepo.size());
        assertEquals(1, serviceRepo.size());
        assertEquals(client, clientRepo.findByIdentity("+380971112233").orElseThrow());
        assertEquals(service, serviceRepo.findByIdentity("SRV-1").orElseThrow());
    }
}
